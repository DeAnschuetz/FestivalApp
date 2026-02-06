import React from 'react'
import styles from './LogIn.module.css'
import LogInContainer from './LogInContainer'
import VLPFLogo from '../VLPFLogo.png';

function LogIn() {
    return (
        <div className={styles.Background}>
            <div className={styles.TitelLineOne}>Virtual Line</div>
            <div className={styles.TitelLineTwo}>Party Fast</div>
            <LogInContainer/>
            <img
                src={VLPFLogo}
                style={{
                    position: 'absolute',
                    bottom: 0,
                    right: 0,
                    margin: '0px 12px 24px 0px',
                    borderRadius: '20px'
                }}
            />
        </div>
    )
}

export default LogIn
