provider "azurerm" {
  features {}
}

locals {
  aseName = "core-compute-${var.env}"
  vaultName = "${var.product}-${var.env}"
  local_env = (var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env

  fee_api_url = var.fee_api_url == "" ? "http://fees-register-api-${local.local_env}.service.core-compute-${local.local_env}.internal" : var.fee_api_url

  asp_name = var.env == "prod" ? "div-fps-prod" : "${var.raw_product}-${var.env}"
  asp_rg = var.env == "prod" ? "div-fps-prod" : "${var.raw_product}-${var.env}"
}

data "azurerm_key_vault" "div_key_vault" {
  name                = local.vaultName
  resource_group_name = local.vaultName
}


