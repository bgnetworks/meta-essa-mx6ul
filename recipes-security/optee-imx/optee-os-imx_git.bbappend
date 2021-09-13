do_compile () {
    unset LDFLAGS
    export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
    [ "${TA_SIGN_KEY}" ] && [ "${TA_SIGN_KEY}" != "" ] &&
        export TA_SIGN_KEY="${TA_SIGN_KEY}"

    oe_runmake -C ${S} all CFG_TEE_TA_LOG_LEVEL=0
    # Enable bleow line to build optee OS with debugging logs (& comment above line)
    # oe_runmake CFG_TEE_CORE_LOG_LEVEL=4 -C ${S} all CFG_TEE_TA_LOG_LEVEL=0
}