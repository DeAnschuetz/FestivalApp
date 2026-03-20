import React from 'react'
import styles from './Modules/BtnBar.module.css'

interface Props {}

function BtnBar(props: Props) {
    const {} = props

    return (
        <div className={styles.BtnBar}>
            <div>Alle</div>
            <div>In Arbeit</div>
            <div>Abholbereit</div>
            <div>Abgeschlossen</div>
            <div>Storniert</div>
        </div>
    )
}

export default BtnBar
