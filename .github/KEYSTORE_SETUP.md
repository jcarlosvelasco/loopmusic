
# Keystore Setup for GitHub Actions

This document explains how to configure the necessary secrets in GitHub to automatically sign the release APK.

## Step 1: Create a Keystore

If you don't have a keystore yet, create one with the following command:

```bash
keytool -genkey -v -keystore release-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-release-key
```

**Important:** Keep these safe:
- The keystore password
- The key alias
- The key password

## Step 2: Convert the Keystore to Base64

To store the keystore in GitHub Secrets, you must convert it to Base64:


```bash
base64 -i release-keystore.jks | pbcopy
```


This will copy the Base64 content to the clipboard.

## Step 3: Configure GitHub Secrets

Go to your repository on GitHub and navigate to:
**Settings → Secrets and variables → Actions → New repository secret**

Create the following secrets:

### 1. KEYSTORE_BASE64
- **Name:** `KEYSTORE_BASE64`
- **Value:** Paste the Base64 content of the keystore you copied

### 2. KEYSTORE_PASSWORD
- **Name:** `KEYSTORE_PASSWORD`
- **Value:** The password you used to create the keystore

### 3. KEY_ALIAS
- **Name:** `KEY_ALIAS`
- **Value:** The key alias (for example: `my-release-key`)

### 4. KEY_PASSWORD
- **Name:** `KEY_PASSWORD`
- **Value:** The key password

## Verification

Once the secrets are configured:

1. Make a commit to the `main` branch
2. The workflow will run automatically
3. If the tests pass, a signed APK will be generated
4. The APK will be published as a new release on GitHub

## Security Notes

- **NEVER** commit the `.jks` file to the repository
- **NEVER** share the keystore passwords
- The `release-keystore.jks` file is included in `.gitignore`
- GitHub secrets are encrypted and only exposed during workflow execution

## .gitignore File

Make sure `.gitignore` includes:

```
*.jks
*.keystore
```
