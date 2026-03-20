import React, { useEffect, useRef, useState } from "react";
import styles from "./Modules/HeaderFoodCourt.module.css";
import FoodCourtDropDown from "./FoodCourtDropDown";
import FoodCourtImage from "./FoodCourtImage";


interface HeaderFoodCourtProps {
    title: string;
    loginLabel: string;
    foodCourtId?: string;
    apiBase: string;
    token?: string | null;
    onOpenStand?: () => void;
    onLogout?: () => void;
}

const HeaderFoodCourt: React.FC<HeaderFoodCourtProps> = ({
    title,
    loginLabel,
    foodCourtId,
    apiBase,
    token,
    onOpenStand,
    onLogout,
}: HeaderFoodCourtProps) => {
    const [menuOpen, setMenuOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setMenuOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <div className={styles.TopHeader}>
            <div className={styles.TopLeft}>
                <div className={styles.MenuAnchor} ref={menuRef}>
                    <button className={styles.MenuButton} onClick={() => setMenuOpen((open) => !open)}>
                        <i className="fa fa-bars" />
                    </button>
                    <div className={styles.LoginTag}>{loginLabel}</div>
                    {menuOpen && (
                        <FoodCourtDropDown
                            onOpenStand={() => {
                                setMenuOpen(false);
                                onOpenStand?.();
                            }}
                            onLogout={() => {
                                setMenuOpen(false);
                                onLogout?.();
                            }}
                        />
                    )}
                </div>
                <div className={styles.TopTitle}>{title}</div>
            </div>
            <div className={styles.TopRight}>
                <FoodCourtImage
                    foodCourtId={foodCourtId}
                    apiBase={apiBase}
                    token={token}
                    className={styles.FoodImage}
                />
            </div>
        </div>
    );
}

export default HeaderFoodCourt;