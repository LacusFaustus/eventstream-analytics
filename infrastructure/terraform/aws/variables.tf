variable "aws_region" {
  description = "AWS region"
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name"
  default     = "dev"
}

variable "instance_type" {
  description = "EC2 instance type"
  default     = "t3.medium"
}