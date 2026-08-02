from airflow.models import BaseOperator
from airflow.providers.clickhouse.hooks.clickhouse import ClickHouseHook
from airflow.utils.decorators import apply_defaults

class ClickHouseToS3Operator(BaseOperator):
    """
    Operator для выгрузки данных из ClickHouse в S3
    """
    template_fields = ('query', 's3_key')

    @apply_defaults
    def __init__(
        self,
        query: str,
        s3_key: str,
        clickhouse_conn_id: str = 'clickhouse_default',
        *args,
        **kwargs
    ):
        super().__init__(*args, **kwargs)
        self.query = query
        self.s3_key = s3_key
        self.clickhouse_conn_id = clickhouse_conn_id

    def execute(self, context):
        hook = ClickHouseHook(clickhouse_conn_id=self.clickhouse_conn_id)
        result = hook.get_pandas_df(self.query)
        result.to_parquet(self.s3_key, index=False)
        self.log.info(f"Data exported to {self.s3_key}")