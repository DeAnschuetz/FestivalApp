import React from "react";
import styles from "./Modules/BtnBar.module.css";

interface BtnBarProps {
  filter: string;
  setFilter: React.Dispatch<React.SetStateAction<string>>;
}

function BtnBar(props: BtnBarProps) {
  const { filter, setFilter } = props;

  const filters = [
    { label: "Alle", value: "" },
    { label: "In Arbeit", value: "in_progress" },
    { label: "Abholbereit", value: "ready_for_pickup" },
    { label: "Abgeschlossen", value: "done" },
    { label: "Storniert", value: "canceled" },
  ];

  return (
    <div className={styles.BtnBar}>
      {filters.map((item, index) => (
        <div
          key={index}
          className={filter === item.value ? styles.Active : styles.BtnHover}
          onClick={() => setFilter(item.value)}
        >
          {item.label}
        </div>
      ))}
    </div>
  );
}

export default BtnBar;
