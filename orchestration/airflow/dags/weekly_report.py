from datetime import datetime, timedelta
from airflow import DAG
from airflow.operators.python import PythonOperator
from airflow.providers.clickhouse.operators.clickhouse import ClickHouseOperator
from airflow.operators.email import EmailOperator

default_args = {
    'owner': 'data_team',
    'depends_on_past': False,
    'start_date': datetime(2026, 1, 1),
    'email_on_failure': ['data@company.com'],
    'retries': 2,
    'retry_delay': timedelta(minutes=5)
}

dag = DAG(
    'weekly_report',
    default_args=default_args,
    description='Weekly analytics report',
    schedule_interval='0 8 * * 1',  # Каждый понедельник в 8:00
    catchup=False,
    tags=['report', 'weekly']
)

# Генерация еженедельного отчета
generate_weekly_report = ClickHouseOperator(
    task_id='generate_weekly_report',
    clickhouse_conn_id='clickhouse_default',
    sql="""
        INSERT INTO weekly_reports
        SELECT
            toStartOfWeek(date) as week_start,
            COUNT(DISTINCT user_id) as weekly_active_users,
            AVG(events_per_user) as avg_events_per_user,
            SUM(total_events) as total_weekly_events
        FROM daily_metrics
        WHERE date >= toStartOfWeek(now())
        GROUP BY week_start
    """,
    dag=dag
)

# Отправка email отчета
send_email_report = EmailOperator(
    task_id='send_email_report',
    to='team@company.com',
    subject='📊 Weekly Analytics Report',
    html_content='''
    <h1>Weekly Analytics Report</h1>
    <p>Еженедельный отчет сгенерирован успешно.</p>
    <p>Просмотреть дашборд: <a href="http://grafana:3000">Grafana</a></p>
    ''',
    dag=dag
)

generate_weekly_report >> send_email_report