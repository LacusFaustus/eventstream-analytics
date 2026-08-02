provider "aws" {
  region = var.aws_region
}

# S3 Bucket для Data Lake
resource "aws_s3_bucket" "analytics_bucket" {
  bucket = "eventstream-analytics-${var.environment}"
  force_destroy = true

  tags = {
    Name        = "EventStream Analytics"
    Environment = var.environment
  }
}

resource "aws_s3_bucket_versioning" "analytics_bucket" {
  bucket = aws_s3_bucket.analytics_bucket.id
  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_lifecycle_configuration" "analytics_bucket" {
  bucket = aws_s3_bucket.analytics_bucket.id

  rule {
    id     = "expire_old_data"
    status = "Enabled"

    expiration {
      days = 30
    }
  }
}

# IAM Role для EKS
resource "aws_iam_role" "eks_cluster" {
  name = "eks-cluster-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "eks.amazonaws.com"
      }
    }]
  })
}

# VPC
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    Name = "eventstream-${var.environment}"
  }
}

# Subnets
resource "aws_subnet" "public" {
  count = 2
  vpc_id = aws_vpc.main.id
  cidr_block = cidrsubnet(aws_vpc.main.cidr_block, 8, count.index)
  availability_zone = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name = "public-${count.index}"
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}

output "s3_bucket_name" {
  value = aws_s3_bucket.analytics_bucket.bucket
}

output "vpc_id" {
  value = aws_vpc.main.id
}

output "subnet_ids" {
  value = aws_subnet.public[*].id
}