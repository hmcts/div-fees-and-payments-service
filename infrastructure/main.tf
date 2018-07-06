provider "vault" {
  //  # It is strongly recommended to configure this provider through the
  //  # environment variables described above, so that each user can have
  //  # separate credentials set in the environment.
  //  #
  //  # This will default to using $VAULT_ADDR
  //  # But can be set explicitly
  address = "https://vault.reform.hmcts.net:6200"
}

locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"

  fee_api_url = "${var.fee_api_url == "" ? "http://fee-api-${local.local_env}.service.core-compute-${local.local_env}.internal" : var.fee_api_url}"
}


module "div-fees-and-payment-service" {
  source = "git@github.com:hmcts/moj-module-webapp.git"
  product = "${var.product}-${var.reform_service_name}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  is_frontend = false
  subscription = "${var.subscription}"
  common_tags  = "${var.common_tags}"

  app_settings = {
    //    logging vars
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.reform_service_name}"
    REFORM_ENVIRONMENT = "${var.env}"
    FEE_API_URL = "${local.fee_api_url}${var.fee_api_endpoint}"
  }
}

