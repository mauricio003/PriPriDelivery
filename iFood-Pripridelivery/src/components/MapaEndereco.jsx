import React, { useEffect, useRef, useState } from "react";
import {
  GoogleMap,
  Marker,
  StandaloneSearchBox,
  useJsApiLoader,
} from "@react-google-maps/api";

const libraries = ["places"];

const containerStyle = {
  width: "100%",
  height: "300px",
  borderRadius: "12px",
};

const centroPadrao = {
  lat: -23.6824,
  lng: -46.5654,
};

export default function MapaEndereco({ onSelecionarLocal, enderecoBusca }) {
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
    libraries,
  });

  const [mapa, setMapa] = useState(null);
  const [posicao, setPosicao] = useState(centroPadrao);
  const [enderecoTexto, setEnderecoTexto] = useState("");
  const searchBoxRef = useRef(null);

  useEffect(() => {
    if (!navigator.geolocation) return;

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const atual = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        setPosicao(atual);
      },
      () => {
        // mantém centro padrão
      }
    );
  }, []);

  useEffect(() => {
  if (!isLoaded || !enderecoBusca || !window.google) return;

  const geocoder = new window.google.maps.Geocoder();

  geocoder.geocode({ address: enderecoBusca }, (results, status) => {
    if (status === "OK" && results[0]) {
      const location = results[0].geometry.location;

      const novaPosicao = {
        lat: location.lat(),
        lng: location.lng(),
      };

      setPosicao(novaPosicao);
      setEnderecoTexto(results[0].formatted_address || enderecoBusca);

      if (mapa) {
        mapa.panTo(novaPosicao);
        mapa.setZoom(17);
      }

      if (onSelecionarLocal) {
        onSelecionarLocal({
          endereco: results[0].formatted_address || enderecoBusca,
          latitude: novaPosicao.lat,
          longitude: novaPosicao.lng,
        });
      }
    }
  });
}, [enderecoBusca, isLoaded, mapa, onSelecionarLocal]);

  const onLoadMap = (map) => {
    setMapa(map);
  };

  const onUnmountMap = () => {
    setMapa(null);
  };

  const onLoadSearchBox = (ref) => {
    searchBoxRef.current = ref;
  };

  const onPlacesChanged = () => {
    const places = searchBoxRef.current?.getPlaces();

    if (!places || places.length === 0) return;

    const place = places[0];

    if (!place.geometry || !place.geometry.location) return;

    const novaPosicao = {
      lat: place.geometry.location.lat(),
      lng: place.geometry.location.lng(),
    };

    const enderecoFormatado = place.formatted_address || place.name || "";

    setPosicao(novaPosicao);
    setEnderecoTexto(enderecoFormatado);

    if (mapa) {
      mapa.panTo(novaPosicao);
      mapa.setZoom(17);
    }

    if (onSelecionarLocal) {
      onSelecionarLocal({
        endereco: enderecoFormatado,
        latitude: novaPosicao.lat,
        longitude: novaPosicao.lng,
      });
    }
  };

  if (!isLoaded) {
    return <div>Carregando mapa...</div>;
  }

  return (
    <div className="space-y-3">
      <StandaloneSearchBox
        onLoad={onLoadSearchBox}
        onPlacesChanged={onPlacesChanged}
      >
        <input
          type="text"
          placeholder="Digite seu endereço"
          value={enderecoTexto}
          onChange={(e) => setEnderecoTexto(e.target.value)}
          className="w-full border border-gray-300 rounded-lg px-4 py-3 outline-none"
        />
      </StandaloneSearchBox>

      <GoogleMap
        mapContainerStyle={containerStyle}
        center={posicao}
        zoom={15}
        onLoad={onLoadMap}
        onUnmount={onUnmountMap}
      >
        <Marker position={posicao} />
      </GoogleMap>
    </div>
  );
}