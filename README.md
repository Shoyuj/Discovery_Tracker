# 🎵 Discovery Tracker

> Track how much of an artist's discography you've actually heard.

## What it does

Ever wonder how much of Kanye's catalog you've listened to? Discovery Tracker lets you search any artist, automatically imports their entire discography from Spotify, and lets you mark songs as listened. You get a real-time completion percentage per artist.

## Features

- 🔍 Search any artist by name
- 🎶 Auto-import full discography via Spotify API
- ✅ Mark songs as listened / unheard
- 📊 Real-time completion percentage
- 💾 Data persists across sessions

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** PostgreSQL (Docker)
- **Frontend:** HTML, CSS, Vanilla JavaScript
- **API:** Spotify Web API (OAuth 2.0 Client Credentials)

## Setup

1. Clone the repo
2. Copy `application-example.yaml` to `application.yaml` and add your credentials:
    - Spotify Client ID and Secret from [Spotify Developer Dashboard](https://developer.spotify.com)
    - PostgreSQL credentials
3. Start the database:
```bash
   docker compose up -d
```
4. Run the Spring Boot app
5. Open `Frontend/index.html` in your browser

## How it works

When you search an artist, the backend authenticates with Spotify using Client Credentials Flow, fetches the artist's albums, then pulls every track from each album and stores them in PostgreSQL. You can then mark songs as listened and see your completion percentage update in real time.

## Screenshots

