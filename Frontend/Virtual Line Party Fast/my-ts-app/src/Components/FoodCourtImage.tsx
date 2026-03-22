import React, { useEffect, useMemo, useState } from "react";

interface FoodCourtImageProps {
  foodCourtId?: string;
  apiBase: string;
  token?: string | null;
  className: string;
  alt?: string;
}

const isUuid = (value: string) =>
  /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/.test(value);

function FoodCourtImage({ foodCourtId, apiBase, token, className, alt = "Food Court" }: FoodCourtImageProps) {
  const [fallbackImageUrl, setFallbackImageUrl] = useState("");
  const [useFallbackImage, setUseFallbackImage] = useState(false);

  const directImageUrl = useMemo(() => {
    if (!foodCourtId || !isUuid(foodCourtId)) {
      return "";
    }

    return `${apiBase}/ffb/food_court/image/${foodCourtId}`;
  }, [apiBase, foodCourtId]);

  useEffect(() => {
    setUseFallbackImage(false);
    setFallbackImageUrl((currentUrl) => {
      if (currentUrl) {
        URL.revokeObjectURL(currentUrl);
      }
      return "";
    });
  }, [foodCourtId]);

  useEffect(() => {
    return () => {
      if (fallbackImageUrl) {
        URL.revokeObjectURL(fallbackImageUrl);
      }
    };
  }, [fallbackImageUrl]);

  const loadFallbackImage = async () => {
    if (!foodCourtId || !isUuid(foodCourtId)) {
      return;
    }

    const headers: Record<string, string> = {
      Accept: "image/png",
    };

    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }

    try {
      const response = await fetch(`${apiBase}/ffb/food_court/image/${foodCourtId}`, {
        method: "GET",
        headers,
        credentials: "include",
      });

      if (!response.ok) {
        return;
      }

      const blob = await response.blob();
      const objectUrl = URL.createObjectURL(blob);
      setFallbackImageUrl((currentUrl) => {
        if (currentUrl) {
          URL.revokeObjectURL(currentUrl);
        }
        return objectUrl;
      });
      setUseFallbackImage(true);
    } catch {
      setFallbackImageUrl("");
    }
  };

  if (!directImageUrl && !fallbackImageUrl) {
    return <div className={className} />;
  }

  return (
    <img
      className={className}
      src={useFallbackImage ? fallbackImageUrl : directImageUrl}
      alt={alt}
      onError={() => {
        if (!useFallbackImage) {
          loadFallbackImage();
        }
      }}
    />
  );
}

export default FoodCourtImage;
