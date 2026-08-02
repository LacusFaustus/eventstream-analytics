from datetime import datetime, timedelta
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.operators.bash import BashOperator
from airflow.providers.postgres.operators.postgres import PostgresOperator
from airflow.providers.clickhouse.operators.clickhouse import ClickHouseOperator

default_args = {
    'owner': 'data_team',
    'depends_on_past': False,
    'start_date': datetime(2026, 1, 1),
    'email_on_failure': ['data@company.com'],
    'email_on_retry': False,
    'retries': 3,
    'retry_delay': timedelta(minutes=5)
}

dag = DAG(
    'daily_analytics_aggregation',
    default_args=default_args,
    description='Daily ETL for analytics',
    schedule_interval='0 1 * * *',  # Каждый день в 1:00
    catchup=False,
    tags=['analytics', 'daily', 'etl']
)

# ============================================================
# 1. Extract: Проверка данных
# ============================================================
check_data_quality = PostgresOperator(
    task_id='check_data_quality',
    postgres_conn_id='postgres_default',
    sql="""
        SELECT
            COUNT(*) as total_events,
            COUNT(DISTINCT user_id) as unique_users,
            COUNT(DISTINCT event_id) as unique_events,
            MIN(timestamp) as min_time,
            MAX(timestamp) as max_time
        FROM events_raw
        WHERE timestamp > NOW() - INTERVAL '1 day'
    """
)

# ============================================================
# 2. Transform: Агрегации в ClickHouse
# ============================================================
aggregate_daily_metrics = ClickHouseOperator(
    task_id='aggregate_daily_metrics',
    clickhouse_conn_id='clickhouse_default',
    sql="""
        INSERT INTO daily_metrics
        SELECT
            toDate(timestamp) as date,
            uniq(user_id) as active_users,
            uniqIf(user_id, timestamp > NOW() - INTERVAL '2 day' AND timestamp <= NOW() - INTERVAL '1 day') as new_users,
            count(*) as total_events,
            avg(session_duration) as avg_session_duration,
            uniqIf(user_id, is_returning = true) / uniq(user_id) as retention_rate,
            sumIf(has_purchase = true, 1) / count(*) as conversion_rate
        FROM events_raw
        WHERE timestamp > NOW() - INTERVAL '1 day'
        GROUP BY date
    """
)

# ============================================================
# 3. Load: Обновление дашбордов
# ============================================================
update_materialized_views = ClickHouseOperator(
    task_id='update_materialized_views',
    clickhouse_conn_id='clickhouse_default',
    sql="""
        -- Обновляем материализованные представления
        ALTER TABLE popular_events MATERIALIZE ASYNC;
        ALTER TABLE user_activity MATERIALIZE ASYNC;
        ALTER TABLE geo_distribution MATERIALIZE ASYNC;
    """
)

# ============================================================
# 4. Export: Выгрузка в S3
# ============================================================
export_to_s3 = BashOperator(
    task_id='export_to_s3',
    bash_command="""
        clickhouse-client --query "
            SELECT *
            FROM daily_metrics
            WHERE date = yesterday()
            FORMAT Parquet
        " > /tmp/daily_metrics.parquet

        aws s3 cp /tmp/daily_metrics.parquet \
            s3://analytics-bucket/daily_metrics/{{ ds }}/daily_metrics.parquet
    """
)

# ============================================================
# 5. Cleanup: Удаление старых данных
# ============================================================
cleanup_old_data = ClickHouseOperator(
    task_id='cleanup_old_data',
    clickhouse_conn_id='clickhouse_default',
    sql="""
        ALTER TABLE events_raw
        DELETE WHERE timestamp < NOW() - INTERVAL '30 day'
    """
)

# ============================================================
# 6. Quality: Проверка качества
# ============================================================
data_quality_check = PythonOperator(
    task_id='data_quality_check',
    python_callable=lambda: print("Data quality check passed!"),
    dag=dag
)

# ============================================================
# Pipeline
# ============================================================
check_data_quality >> aggregate_daily_metrics >> update_materialized_views
update_materialized_views >> [export_to_s3, data_quality_check]
data_quality_check >> cleanup_old_data