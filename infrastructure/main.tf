locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"

  fee_api_url = "${var.fee_api_url == "" ? "http://fees-register-api-${local.local_env}.service.core-compute-${local.local_env}.internal" : var.fee_api_url}"

  asp_name = "${var.env == "prod" ? "div-fps-prod" : "${var.product}-${var.env}"}"
  asp_rg = "${var.env == "prod" ? "div-fps-prod" : "${var.product}-shared-infrastructure-${var.env}"}"
}


module "div-fees-and-payment-service" {
  source                          = "git@github.com:hmcts/moj-module-webapp.git"
  product                         = "${var.product}-${var.reform_service_name}"
  location                        = "${var.location}"
  env                             = "${var.env}"
  ilbIp                           = "${var.ilbIp}"
  is_frontend                     = false
  subscription                    = "${var.subscription}"
  common_tags                     = "${var.common_tags}"
  asp_name                        = "${local.asp_name}"
  asp_rg                          = "${local.asp_rg}"

  app_settings = {
    //    logging vars
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.reform_service_name}"
    REFORM_ENVIRONMENT = "${var.env}"
    FEE_API_URL = "${local.fee_api_url}${var.fee_api_endpoint}"
  }
}

