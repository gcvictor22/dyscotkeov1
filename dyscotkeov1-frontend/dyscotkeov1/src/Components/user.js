import axios, { } from "axios";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

export const Profile = () => {

    const {userName} = useParams();
    var url = `/user/userName/${userName}`

    var loggedUser = localStorage.getItem('loggedUser');

    const [fullName, setFullName] = useState('');
    const navigate = useNavigate();

    const apiResponse = () => {
        axios.get(url, { headers: headers }).then((res) => {
            setFullName(res.data.fullName);
        }).catch((er) => {
            if (loggedUser === userName) {
                window.location.reload()
            }else if(er.response.status === 404){
                navigate("*");
            }else{
                Swal.fire({
                    icon: 'info',
                    title: 'No puedes acceder a esta sección sin iniciar sesión',
                    showDenyButton: true,
                    denyButtonColor: '#4c007d',
                    confirmButtonText: 'Iniciar sesión',
                    confirmButtonColor: '#1465bb',
                    denyButtonText: 'Registrarme',
                }).then((result) => {
                    if (result.isDenied) {
                        navigate("/register")
                    }else{
                        navigate("/")
                    }
                })
            }
        })
    }

    return (
        <div>
            {apiResponse()}
            {fullName}
        </div>
    );
}

export default Profile;