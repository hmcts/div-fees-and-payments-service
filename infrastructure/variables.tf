variable "reform_service_name" {
    default = "fps"
}

variable "reform_team" {
    default = "div"
}

variable "env" {
    type = "string"
}

variable "product" {
    type    = "string"
}

variable "raw_product" {
  default = "div"
}

variable "tenant_id" {}

variable "client_id" {
    description = "(Required) The object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies. This is usually sourced from environment variables and not normally required to be specified."
}

variable "jenkins_AAD_objectId" {
    type        = "string"
    description = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "idam_s2s_url_prefix" {
    default = "rpe-service-auth-provider"
}

variable "auth_provider_service_client_microservice" {
    default = ""
}

variable "auth_provider_service_client_key" {
    default = ""
}

variable "auth_provider_service_client_tokentimetoliveinseconds" {
    default = "900"
}

variable "logging_level_org_springframework_web" {
    type = "string"
}

variable "logging_level_uk_gov_hmcts_ccd" {
    type = "string"
}

variable "fee_api_url" {
    type = "string"
    default = ""
}

variable "subscription" {}

variable "location" {
    type = "string"
    default = "UK South"
}

variable "ilbIp" {}

variable "vault_env" {}

variable "common_tags" {
    type = "map"
}

variable "appinsights_instrumentation_key" {
  description = "Instrumentation key of the App Insights instance this webapp should use. Module will create own App Insights resource if this is not provided"
  default = ""
}

variable "health_check_ttl" {
    type = "string"
    value = "5000"
}
