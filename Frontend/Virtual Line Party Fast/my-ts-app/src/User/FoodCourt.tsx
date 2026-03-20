import React, { useEffect } from "react";

interface Props {}

function FoodCourt(props: Props) {
  const {} = props;

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://10.45.129.19:8080/product/list/by_food_court_id/a6caa51c-52e0-4ea8-80bc-bcf3d2c03efc",
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            credentials: "include",
          },
        );

        if (!response.ok) {
          throw new Error("Failed to fetch");
        }

        const result = await response.json();
        console.log("result hier: ", result)
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchData();
  }, [token]);

  return <div></div>;
}

export default FoodCourt;
