boot medium: SD Card
class - 10
size - 16 GB

write command:

```bash
rm -f /dmblk/1.txt
sync; echo 3 > /proc/sys/vm/drop_caches
dd if=/dev/zero of=/dmblk/1.txt bs=1M count=100 && sync
```

Writing 100MB data (in chunks of 1MB) to the boot medium

read command:

```bash
sync; echo 3 > /proc/sys/vm/drop_caches
dd if=/dmblk/1.txt of=/dev/null bs=1M count=100 && sync
```

Reading the written data in chunks of 1 MB from boot medium

## Plain block readings

as a base line

| S.<d/>No | Write (MB/s) | Read (MB/s) |
| -------- | ------------ | ----------- |
| 1        | 21.2         | 23.0        |
| 2        | 24.3         | 23.0        |
| 3        | 29.4         | 22.7        |
| 4        | 22.4         | 23.0        |
| 5        | 26.6         | 23.0        |

- table readings are top 5 on 10 iterations

---

## Which cipher:hash combination?

run `cryptsetup -c ... -s ... benchmark`. Issuing the command without `-c` and `-s` runs the benchmark for a number of different choices.

```log
root@imx6ulevk:~# cryptsetup benchmark
# Tests are approximate using memory only (no storage IO).
#     Algorithm |       Key |      Encryption |      Decryption
        aes-cbc        256b        24.4 MiB/s        23.6 MiB/s <---accelerated by CAAM
        aes-xts        256b        18.9 MiB/s        18.8 MiB/s <---no hardware acceleration
```

DM-Crypt performs cryptographic operations via the interfaces provided by the Linux kernel crypto API. The kernel crypto API defines a standard, extensible interface to ciphers and other data transformations implemented in the kernel (or as loadable modules). DM-Crypt parses the cipher specification `aes-cbc-essiv:sha256` passed as part of its mapping table and instantiates the corresponding transforms via the kernel crypto API.

To list the available cryptographic transformations on the target:

```log
root@imx6ulevk:~# cat /proc/crypto
name         : echainiv(authenc(hmac(sha256),cbc(aes)))
driver       : echainiv-authenc-hmac-sha256-cbc-aes-caam
module       : kernel
priority     : 3000
refcnt       : 1
selftest     : passed
internal     : no
type         : aead
async        : yes
blocksize    : 16
ivsize       : 16
maxauthsize  : 32
geniv        : <none>
```

**NOTE**: AES in XTS is the most convenient cipher for block-oriented storage devices and shows significant performance gain, however, this mode is not natively supported on i.<d/>MX’s CAAM module.

| S.<d/>No | Write (MB/s) | Read (MB/s) |
| -------- | ------------ | ----------- |
| 1        | 14.0         | 10.7        |
| 2        | 17.2         | 10.6        |
| 3        | 14.0         | 10.7        |
| 4        | 13.4         | 10.6        |
| 5        | 15.2         | 10.7        |

---

| S.<d/>No | Write (MB/s) | Read (MB/s) |
| -------- | ------------ | ----------- |
| 1        | 000          | 0000        |
| 2        | 000          | 0000        |
| 3        | 000          | 0000        |
| 4        | 000          | 0000        |
| 5        | 000          | 0000        |

---

# References:

1. AN12714 i.<d/>MX Encrypted Storage Using CAAM Secure Keys - https://www.mouser.com/pdfDocs/AN12714.pdf
2. Linux Cryptographic Acceleration on an i.<d/>MX6 - http://events17.linuxfoundation.org/sites/events/files/slides/2017-02%20-%20ELC%20-%20Hudson%20-%20Linux%20Cryptographic%20Acceleration%20on%20an%20MX6.pdf
3. Dm-crypt - https://wiki.gentoo.org/wiki/Dm-crypt
4. Dm-crypt full disk encryption - https://wiki.gentoo.org/wiki/Dm-crypt_full_disk_encryption
5. cryptsetup - Linux man page - https://linux.die.net/man/8/cryptsetup
