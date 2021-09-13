do_compile () {
    if [ ${DEFAULTTUNE} = "aarch64" ]; then
        oe_runmake -C ${S} ARCH=arm64
    else
		# Added optimization flag, else error occurs
		# https://github.com/FRRouting/frr/issues/1879#issuecomment-375023394
        oe_runmake -C ${S} ARCH=arm CFLAGS="-O0 -D_FORTIFY_SOURCE=2"
        # Enable bleow line to build optee client with debugging logs (& comment above line)
        # oe_runmake CFG_TEE_CLIENT_LOG_LEVEL=4 CFG_TEE_SUPP_LOG_LEVEL=4 -C ${S} ARCH=arm CFLAGS="-O0 -D_FORTIFY_SOURCE=2"
    fi
}
