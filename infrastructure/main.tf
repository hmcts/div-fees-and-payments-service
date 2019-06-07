locals {
  aseName = "core-compute-${var.env}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"

  fee_api_url = "${var.fee_api_url == "" ? "http://fees-register-api-${local.local_env}.service.core-compute-${local.local_env}.internal" : var.fee_api_url}"

  asp_name = "${var.env == "prod" ? "div-fps-prod" : "${var.raw_product}-${var.env}"}"
  asp_rg = "${var.env == "prod" ? "div-fps-prod" : "${var.raw_product}-${var.env}"}"
}


module "div-fees-and-payment-service" {
  source                          = "git@github.com:hmcts/moj-module-webapp.git"
  product                         = "${var.product}-${var.reform_service_name}"
  location                        = "${var.location}"
  env                             = "${var.env}"
  ilbIp                           = "${var.ilbIp}"
  is_frontend                     = false
  subscription                    = "${var.subscription}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags                     = "${var.common_tags}"
  asp_name                        = "${local.asp_name}"
  asp_rg                          = "${local.asp_rg}"
  instance_size                   = "${var.instance_size}"

  app_settings = {
    //    logging vars
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.reform_service_name}"
    REFORM_ENVIRONMENT = "${var.env}"
    FEE_API_URL = "${local.fee_api_url}"
    MANAGEMENT_ENDPOINT_HEALTH_CACHE_TIMETOLIVE = "${var.health_check_ttl}"
  }
}

