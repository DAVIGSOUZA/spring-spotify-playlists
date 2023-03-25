function TopArtists() {
        fetch("http://localhost:8080/api/user-top-artists")
            .then(response => response.json())
            .then(data => {
                console.log(data)
            })
            .catch(error=>console.log(error));
}