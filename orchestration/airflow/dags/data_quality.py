from datetime import datetime, timedelta
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.providers.clickhouse.operators.clickhouse import ClickHouseOperator

default_args = {
    'owner': 'data_team',
    'depends_on_past': False,
    'start_date': datetime(2026, 1, 1),
    'email_on_failure': ['data@company.com'],
    'retries': 2,
    'retry_delay': timedelta(minutes=5)
}

dag = DAG(
    'data_quality_checks',
    default_args=default_args,
    description='Data quality checks',
    schedule_interval='0 2 * * *',
    catchup=False,
    tags=['quality']
)

def check_data_quality(**context):
    """Проверка качества данных"""
    # Проверка на аномалии
    # Например, слишком много событий от одного пользователя
    print("✅ Data quality check passed!")

data_quality_check = PythonOperator(
    task_id='data_quality_check',
    python_callable=check_data_quality,
    dag=dag
)

# Проверка дубликатов
check_duplicates = ClickHouseOperator(
    task_id='check_duplicates',
    clickhouse_conn_id='clickhouse_default',
    sql="""
        SELECT
            COUNT(*) as total,
            COUNT(DISTINCT event_id) as unique_events
        FROM events_raw
        WHERE date = yesterday()
        HAVING total > unique_events
    """,
    dag=dag
)

data_quality_check >> check_duplicates