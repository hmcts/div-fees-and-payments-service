output "test_environment" {
  value = local.local_env
}

output "vaultUri" {
  value = data.azurerm_key_vault.div_key_vault.vault_uri
}

output "vaultName" {
  value = local.vaultName
}