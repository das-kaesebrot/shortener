#!/bin/bash

JWT_KEYPAIR_FILE_NAME_BASE="config/jwt"

PRIVKEY="$JWT_KEYPAIR_FILE_NAME_BASE.key"
PUBKEY="$JWT_KEYPAIR_FILE_NAME_BASE.pub"

if [ ! -f "$PUBKEY" ]; then
  echo "Generating JWT keypair because it doesn't exist yet"
  echo "Writing $PRIVKEY"
  openssl genpkey -algorithm RSA -out "$PRIVKEY" -outform PEM
  echo "Writing $PUBKEY"
  openssl rsa -pubout -in "$PRIVKEY" -out "$PUBKEY"
fi

java -jar app.jar
