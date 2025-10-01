const baseUrl = "/api/weather";

const weatherImages = {
  "clear sky": "/images/clear.jpg",
  "few clouds": "/images/fewCloud.jpg",
  "scattered clouds": "/images/scatteredClouds.jpg",
  "broken clouds": "/images/brokenClouds.jpg",
  "overcast clouds": "/images/overcastClouds.webp",
  "light rain": "/images/lightRain.jpg",
  "moderate rain": "/images/lightRain.jpg",
  "heavy intensity rain": "/images/heavyRain.webp",
  "snow": "/images/snow.jpg",
  "light snow": "/images/snow.jpg",
  "thunderstorm": "/images/thunderstorm.jpg",
  "mist": "/images/fog.jpg",
  "fog": "/images/fog.jpg",
};

function Header() {
  return <h1 className="header-title">☀️Application Météo</h1>;
}

function SearchBar({ setInfo }) {
  async function handleSubmit(e) {
    e.preventDefault();
    const city = e.target.query.value.trim();
    const query = baseUrl + `?city=${city}`;
    let backendMessage = "";
    try {
      const response = await fetch(query);
      const data = await response.json();
      if (!response.ok) {
        backendMessage = data.error;
        throw new Error(`Oups ! ${backendMessage} (${response.status})`);
      }
      setInfo(data);
      console.log("Données :", data);
    } catch (error) {
      console.error(error.message);
      window.addErrorMessage(backendMessage);
    }
  }
  return (
    <div className="search-bar">
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="query"
          placeholder="Rechercher une ville..."
          required
        />
        <button type="submit">
          <img src="/images/icons8-search.svg" alt="Search Icon" />
        </button>
      </form>
    </div>
  );
}

function Main({ info }) {
  return (
    <div className="display-info">
      {info ? (
        weatherImages[info.description] ? (
          <img src={weatherImages[info.description]} alt="Wheather Image" />
        ) : (
          <img src="../images/meteo_img.jpg" alt="Image météo par défaut" />
        )
      ) : (
        <img src="../images/meteo_img.jpg" alt="Image météo par défaut" />
      )}
      <div className="info">
        {info ? (
          <div>
            <h2>Ville : {info.city}</h2>
            <h2>Humidité : {info.humidity}%</h2>
            <h2>Température : {info.temperature}°C</h2>
            <h2>Description : {info.description}</h2>
          </div>
        ) : (
          <p>Aucune info pour l'instant</p>
        )}
      </div>
    </div>
  );
}

function App() {
  const [info, setInfo] = React.useState(null);
  return (
    <div>
      <Header />
      <SearchBar setInfo={setInfo} />
      <Main info={info} />
    </div>
  );
}

ReactDOM.render(<App />, document.getElementById("wheater-container"));
