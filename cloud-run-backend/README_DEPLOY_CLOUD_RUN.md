# SUPARBASE TRADING BOT - GOOGLE CLOUD RUN DEPLOYMENT GUIDE

This guide walks you through deploying the **SUPARBASE 24/7 SMC Forex Trading Engine** to Google Cloud Run for non-stop automated trading.

---

## 1. Prerequisites

1. Install Google Cloud SDK (`gcloud` CLI):
   ```bash
   gcloud init
   gcloud auth login
   ```
2. Enable required Google Cloud services:
   ```bash
   gcloud services enable run.googleapis.com containerregistry.googleapis.com cloudscheduler.googleapis.com
   ```

---

## 2. Configure Environment Variables

Edit `cloud-run-backend/.env` with your broker and alert keys:

```ini
PORT=8080
BROKER_TYPE=META_API
METAAPI_TOKEN=your_metaapi_token
METAAPI_ACCOUNT_ID=your_mt5_account_id
TELEGRAM_BOT_TOKEN=your_bot_token
TELEGRAM_CHAT_ID=your_chat_id
ACCOUNT_RISK_PERCENT=1.0
PAIRS=EURUSD,GBPUSD,XAUUSD,GBPJPY
SIMULATION_MODE=true
```

---

## 3. Deploy in One Command

Run from the `cloud-run-backend` directory:

```bash
gcloud run deploy suparbase-trading-bot \
  --source . \
  --platform managed \
  --region europe-west1 \
  --allow-unauthenticated \
  --min-instances 1 \
  --memory 512Mi \
  --set-env-vars SIMULATION_MODE=true,ACCOUNT_RISK_PERCENT=1.0
```

> **Note**: `--min-instances 1` ensures Google Cloud Run keeps at least 1 warm instance running 24/7 so your 1-minute SMC cron job never sleeps!

---

## 4. (Optional) Set up Cloud Scheduler Keep-Alive

To guarantee zero downtime and ping your `/health` endpoint:

```bash
gcloud scheduler jobs create http suparbase-heartbeat \
  --schedule="* * * * *" \
  --uri="https://YOUR-CLOUD-RUN-URL/health" \
  --http-method=GET
```

Your automated SMC bot is now operating 24/7 on Google Cloud!
