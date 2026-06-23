document.getElementById("addArtistBtn").addEventListener("click", addArtist);
document.getElementById("artistInput").addEventListener("keypress", function(e) {
    if (e.key === "Enter") {
        addArtist();
    }
});

function addArtist() {
    const artistName = document.getElementById("artistInput").value;
    if (!artistName) return;

    fetch("http://localhost:8080/api/v1/artists/" + artistName, {
        method: "POST"
    })
    .then(response => response.json())
    .then(data => displayArtist(data));
}

function displayArtist(data) {
    const card = document.createElement("div");
    card.id = "artist-" + data.id;
    card.className = "artist-card";
    card.innerHTML = `
        <div class="artist-header">
            <img src="${data.imageUrl}" width="60" height="60">
            <div>
                <h3>${data.name}</h3>
                <p id="stats-${data.id}" class="stats">No songs imported yet</p>
            </div>
        </div>
        <button onclick="fetchSongs(${data.id})">Import Songs</button>
        <ul id="songs-${data.id}" class="song-list"></ul>
    `;
    document.getElementById("artistsContainer").appendChild(card);
}

function fetchSongs(n) {
    fetch("http://localhost:8080/api/v1/artists/" + n + "/songs", {
        method: "POST"
    })
    .then(response => response.json())
    .then(() => {
        getStats(n);
        getSongs(n);
        document.querySelector("#artist-" + n + " button").style.display = "none";
    });
}

function getStats(artistId) {
    fetch("http://localhost:8080/api/v1/artists/" + artistId + "/stats")
    .then(response => response.json())
    .then(data => {
        const stats = document.getElementById("stats-" + artistId);
        stats.innerHTML = data.listened + " / " + data.total + " listened (" + data.percentage + "%)";
    });
}

function getSongs(artistId) {
    fetch("http://localhost:8080/api/v1/songs/" + artistId)
    .then(response => response.json())
    .then(songs => {
        if (songs.length > 0) {
            document.querySelector("#artist-" + artistId + " button").style.display = "none";
        }
        const list = document.getElementById("songs-" + artistId);
        list.innerHTML = "";
        songs.forEach(song => {
            const listItem = document.createElement("li");
            listItem.innerHTML = `
                <input type="checkbox" onchange="updateStatus(${song.id}, this.checked, ${artistId})"
                ${song.status === 'LISTENED' ? 'checked' : ''}>
                ${song.title}
            `;
            list.appendChild(listItem);
        });
    });
}

function updateStatus(songId, isChecked, artistId) {
    const status = isChecked ? "LISTENED" : "UNHEARD";
    fetch("http://localhost:8080/api/v1/songs/" + songId + "/status", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: status })
    })
    .then(() => getStats(artistId));
}

function loadArtists() {
    fetch("http://localhost:8080/api/v1/artists")
    .then(response => response.json())
    .then(artists => {
        artists.forEach(artist => {
            displayArtist(artist);
            getStats(artist.id);
            getSongs(artist.id);
        });
    });
}

loadArtists();