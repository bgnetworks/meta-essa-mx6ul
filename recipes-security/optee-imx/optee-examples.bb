# Modified by Daniel Selvan, Jasmin Infotech

SUMMARY = "OP-TEE examples"
HOMEPAGE = "https://github.com/linaro-swg/optee_examples"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=cd95ab417e23b94f381dafc453d70c30"

inherit pythonnative
DEPENDS = "optee-client-imx optee-os-imx python-pycrypto-native"

SRC_URI = "git://github.com/linaro-swg/optee_examples.git"
SRCREV = "559b2141c16bf0f57ccd72f60e4deb84fc2a05b0"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    if [ ${DEFAULTTUNE} = "aarch64" ];then
        export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64/
        export ARCH=arm64
    else
        export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm32/
        export ARCH=arm
    fi

    export OPTEE_CLIENT_EXPORT=${STAGING_DIR_HOST}/usr
    export HOST_CROSS_COMPILE=${HOST_PREFIX}
    export TA_CROSS_COMPILE=${HOST_PREFIX}
    export TEEC_EXPORT=${STAGING_DIR_HOST}/usr
    [ "${TA_SIGN_KEY}" ] && [ "${TA_SIGN_KEY}" != "" ] &&
        export TA_SIGN_KEY="${TA_SIGN_KEY}"

    oe_runmake V=1
    # Enable bleow line to build optee examples with debugging logs (& comment above line)
    oe_runmake V=1 CFG_TEE_TA_LOG_LEVEL=4
}

do_install () {
    install -d ${D}/usr/bin
    install -D -p -m0755 ${S}/out/ca/* ${D}/usr/bin/

    install -d ${D}/lib/optee_armtz
    find ${S}/out/ta -name '*.ta' | while read name; do
        install -D -p -m 0444 $name ${D}/lib/optee_armtz/
    done
}

FILES_${PN} = "/usr/bin/ /lib*/optee_armtz/"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
