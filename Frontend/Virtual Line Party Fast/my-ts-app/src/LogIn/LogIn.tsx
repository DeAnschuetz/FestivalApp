import React from 'react'
import styles from './Modules/LogIn.module.css'
import VLPFLogo from '../VLPFLogo.png';
import RegisterContainer from './RegisterContainer';
import LogInContainer from './LogInContainer';

type LoginProps = {
    mode: 'login' | 'register'
}




function LogIn(props: LoginProps) {
    const {mode } = props;

    return (
        <div className={styles.Background}>
            <div className={styles.TitelLineOne}>Virtual Line</div>
            <div className={styles.TitelLineTwo}>Party Fast</div>
            {mode === "login" ? <LogInContainer /> : <RegisterContainer />}
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
