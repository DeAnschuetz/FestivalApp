import { useEffect, useState } from "react";
import { getFoodCourtImageUrl } from "../Api/ffb/foodCourtApi";

interface FoodCourtImageProps {
  foodCourtId?: string;
  token?: string | null;
  className: string;
  alt?: string;
}

const isUuid = (value: string) =>
  /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/.test(value);

function FoodCourtImage({ foodCourtId, token, className, alt = "Food Court" }: FoodCourtImageProps) {
  const [imageUrl, setImageUrl] = useState("");

  useEffect(() => {
    let cancelled = false;

    const loadImage = async () => {
      setImageUrl("");

      if (!foodCourtId || !isUuid(foodCourtId)) {
        return;
      }

      try {
        const url = (await getFoodCourtImageUrl(foodCourtId)) ?? "";

        setImageUrl(url);
      } catch {
        if (!cancelled) {
          setImageUrl("");
        }
      }
    };

    loadImage();

  }, [foodCourtId]);

  if (!imageUrl) {
    return <div className={className} />;
  }

  return (
    <img
      className={className}
      src={imageUrl}
      alt={alt}
      onError={() => setImageUrl("")}
    />
  );
}

export default FoodCourtImage;
