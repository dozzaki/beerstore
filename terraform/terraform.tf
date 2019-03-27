terraform {
  backend "s3" {
    bucket = "cloudvisor-terraform-state"
    key = "beerstore-cloudvisor"
    region = "us-east-1"
    profile = "terraform"
  }
}