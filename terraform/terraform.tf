terraform {
  backend "s3" {
    bucket = "cloudvisor-terraform-state"
    key = "cloudvisor-beerstore"
    region = "us-east-1"
  }
}