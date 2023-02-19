import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

export const PageNotFound = () => {

    const navigate = useNavigate();
    const [loggedUser, setLoggedUser] = useState('');

    axios.get("/user/profile", {headers : headers})
    .then((res) => setLoggedUser(res.data.userName))

    const navigateTo = () => {

        if (localStorage.length > 0) {
            navigate(`/user/${loggedUser}`);
        }else{
            navigate("/");
        }
    }

    return (
        <div className="notFoundBody">
            <div>
                <div className="starsec"></div>
                <div className="starthird"></div>
                <div className="starfourth"></div>
                <div className="starfifth"></div>
            </div>
            <div className="lamp__wrap">
                <div className="lamp">
                    <div className="cable"></div>
                    <div className="cover"></div>
                    <div className="in-cover">
                        <div className="bulb"></div>
                    </div>
                    <div className="light"></div>
                </div>
            </div>
            <section className="error">
                <div className="error__content">
                    <div className="error__message message">
                        <h1 className="message__title">ERROR 404</h1>
                        <p className="message__text">Lo sentimos, pero la página que buscas no existe</p>
                    </div>
                    <div className="error__nav e-nav">
                    <button className="e-nav__link buttonNotFound" onClick={() => navigateTo()}></button>
                    </div>
                </div>
            </section>
        </div>
    )

}

export default PageNotFound;