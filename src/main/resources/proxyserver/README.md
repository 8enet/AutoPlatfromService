`openssl pkcs12 -export -in certificate.cer -inkey private-key.pem -out eneCert.pkcs12`

`keytool -v -importkeystore -srckeystore eneCert.pkcs12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype JKS`
