FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# HAB Features
SRC_URI += "file://0001-Enable-secure-boot-support.patch"
SRC_URI += "file://0002-Enable-encrypted-boot-support.patch"

# Added to automate encryption with UUU tool
SRC_URI += "file://0003-Add-fastboot-commands.patch"
