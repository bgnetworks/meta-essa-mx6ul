SUMMARY = "system initial setup script"
SRC_URI = "file://sys_setup.sh"
LICENSE = "CLOSED"

RDEPENDS_${PN} += "bash"

do_install() {
    # Installing the setup script in /data
    install -d ${D}/data
    # install -d ${D}/dmblk
    install -m 0755 ${WORKDIR}/sys_setup.sh ${D}/data/
}

FILES_${PN} += "/data/sys_setup.sh"
# FILES_${PN} += "/dmblk/"
