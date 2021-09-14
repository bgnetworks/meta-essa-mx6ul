FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Dm-crypt fragment for 4.19 kernel
SRC_URI += "file://0001-device-mapper-and-crypt-target.cfg"
SRC_URI += "file://0002-cryptographic-API-functions.cfg"

do_configure_append() {
    cat ../*.cfg >>${B}/.config
    cat ${B}/.config >> /workdir/Desktop/kern.config
}