import axios, { } from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import { faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";



const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

export const Profile = () => {

    const { userName } = useParams();
    var url = `/user/userName/${userName}`

    //var loggedUser = localStorage.getItem('loggedUser');

    const [user, setUser] = useState(null);
    const [img, setImg] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [file, setFile] = useState();
    const [fileName, setFileName] = useState('');
    const [postsPage, setPostsPage] = useState([]);
    const [postLoading, setPostLoading] = useState(true);
    const [pageNumber, setPageNumber] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(url, { headers: headers }).then((res) => {
            setUser(res.data);
        }).catch((er) => {
            console.log(er);
            if (er.response.status === 401 && localStorage.getItem('loggedUser') === userName) {
                window.location.reload();

            } else if (er.response.status === 404) {
                navigate("*");
            } else if (er.response.status === 500) {
                localStorage.clear();
            } else {
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
                    } else {
                        navigate("/")
                    }
                })
            }
        })
        setIsLoading(false);
        // eslint-disable-next-line
    }, []);

    useEffect(() => {
        axios.get(`/post/?page=${pageNumber}`, { headers: headers }).then((res) => {
            setPostsPage(res.data);
        }).catch((er) => {
            console.log(er);
        })
        setPostLoading(false);
        // eslint-disable-next-line
    }, [pageNumber])

    function navigateTo(params) {
        if (params !== userName) {
            navigate(`/user/${params}`)
            window.location.reload()
        }
    }

    const imgSubmit = (e) => {
        e.preventDefault();
        headers["Content-Type"] = "multipart/form-data"
        let formData = new FormData();
        formData.append("file", file);

        axios.post("/file/upload", formData, { headers: headers, }).then((res) => {
            setImg(res.data.uri);
            console.log(img);
            if (res.status === 201) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Foto actualizada con éxito!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.reload();
                })
            }
        }).catch((er) => {
            Swal.fire({
                icon: 'error',
                title: er.response['data']['message'],
                showConfirmButton: false,
                timer: 1500
            })
        })
    }

    if (isLoading) {
        return (
            <div>
                <p>Cargando</p>
            </div>
        )
    } else if (user !== null) {
        return (
            <div>
                <div className="container">

                    <div className="userNameProfile">
                        <img src={`http://localhost:8080/file/${user.imgPath}`} className="imgUser" alt="" />
                        <h1>{user.userName} {user.verified === true &&
                            <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" />
                        }</h1>
                    </div>
                    <p className="userFullName">{user.fullName}</p>

                    <ul className="nav nav-tabs enlacesPaginacion">
                        {localStorage.getItem("loggedUser") === userName &&
                            <li className="active"><a href="#Posts" data-toggle="tab">Todos los posts</a></li>
                        }
                        {localStorage.getItem("loggedUser") === userName &&
                            <li><a href="#Product" data-toggle="tab">Product</a></li>
                        }
                        {localStorage.getItem("loggedUser") !== userName &&
                            <li className="active"><a href="#Product" data-toggle="tab">Product</a></li>
                        }
                        <li><a href="#CSS" data-toggle="tab">CSS</a></li>
                        <li><a href="#Javascript" data-toggle="tab">Javascript</a></li>
                        <li><a href="#Bootstrap" data-toggle="tab">Bootstrap</a></li>
                        <li><a href="#Jquery" data-toggle="tab">Jqeury</a></li>
                        {localStorage.getItem('loggedUser') === userName &&
                            <li><a href="#Editar" data-toggle="tab">Editar mi perfil</a></li>
                        }
                    </ul>


                    <div className="tab-content">
                        {localStorage.getItem("loggedUser") === userName &&
                            <div id="Posts" className="tab-pane fade in active">
                                <h3>Todos los posts</h3>
                                <div id="divPosts">
                                    {postsPage.content === undefined &&
                                        <p id="noHayPost">No hay ninguna publicación</p>
                                    }
                                    {postLoading === false && postsPage.content !== undefined &&
                                        <div id="paginadorPosts">
                                            <div id="paginacion">
                                                {postsPage.first !== true &&
                                                    <button id="atras" onClick={() => setPageNumber(pageNumber - 1)}>&#8678;</button>
                                                }
                                                {postsPage.totalElements > 20 &&
                                                    <button id="numPagina">{pageNumber + 1}</button>
                                                }
                                                {postsPage.last !== true &&
                                                    <button id="siguiente" onClick={() => setPageNumber(pageNumber + 1)}>&#8680;</button>
                                                }
                                            </div>
                                        </div>
                                    }
                                    <br />
                                    {postLoading === false && postsPage.content !== undefined &&
                                        postsPage.content.map((p) => {
                                            return <div className="post" key={p.id}>
                                                <button className="userWhoPost" onClick={() => navigateTo(p.userWhoPost.userName)}>
                                                    <img src={`http://localhost:8080/file/${p.userWhoPost.imgPath}`} alt="" />
                                                    <p>{p.userWhoPost.userName}</p>
                                                </button>
                                                <h3>{p.affair}</h3>
                                                <p>{p.content}</p>
                                                {p.imgPath.length > 0 && p.imgPath[0] !== "VACIO" &&
                                                    <div className="imgPostContainer">
                                                        {
                                                            p.imgPath.map((i) => {
                                                                return <div className="sigleImgPost" key={i}><img src={`http://localhost:8080/file/${i}`} alt="" /></div>
                                                            })
                                                        }
                                                    </div>
                                                }
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                        }
                        {localStorage.getItem("loggedUser") === userName &&
                            <div id="Product" className="tab-pane fade">
                                <h3>Product</h3>
                                <p>Advanced extended doubtful he he blessing together. Introduced far law gay considered frequently entreaties difficulty. Eat him four are rich nor calm. By an packages rejoiced exercise. To ought on am marry rooms doubt music. Mention entered an through company as. Up arrived no painful between. It declared is prospect an insisted pleasure. </p>
                            </div>
                        }
                        {localStorage.getItem("loggedUser") !== userName &&
                            <div id="Product" className="tab-pane fade in active">
                                <h3>Product</h3>
                                <p>Advanced extended doubtful he he blessing together. Introduced far law gay considered frequently entreaties difficulty. Eat him four are rich nor calm. By an packages rejoiced exercise. To ought on am marry rooms doubt music. Mention entered an through company as. Up arrived no painful between. It declared is prospect an insisted pleasure. </p>
                            </div>
                        }
                        <div id="Editar" className="tab-pane fade">
                            <h3>Modificar foto de perfil</h3>
                            <form onSubmit={imgSubmit}>
                                <div className="wrapper">
                                    <div className="file-upload">
                                        <input type="file" onChange={(e) => { setFile(e.target.files[0]); setFileName(e.target.files[0].name) }} />
                                        <i className="fa fa-upload" aria-hidden="true"></i>
                                    </div>
                                </div>
                                <p id="fileName">{fileName}</p>
                                <input type="submit" value="Guardar" id="submitSaveImg"></input>
                            </form>
                            <hr />
                        </div>
                        <div id="CSS" className="tab-pane fade">
                            <h3>CSS Tutorial</h3>
                            <p>CSS is a stylesheet language that describes the presentation of an HTML (or XML) document. CSS describes how elements must be rendered on screen, on paper, or in other media. This tutorial will teach you CSS from basic to advanced.</p>
                        </div>
                        <div id="Javascript" className="tab-pane fade">
                            <h3>Javascript Tutorial</h3>
                            <p>JavaScript is the programming language of HTML and the Web. Programming makes computers do what you want them to do. JavaScript is easy to learn. This tutorial will teach you JavaScript from basic to advanced.</p>
                        </div>
                        <div id="Bootstrap" className="tab-pane fade">
                            <h3>Bootstrap Tutorial</h3>
                            <p>Bootstrap is the most popular HTML, CSS, and JS framework for developing responsive, mobile first projects on the web.</p>
                        </div>
                        <div id="Jquery" className="tab-pane fade">
                            <h3>Jquery Tutorial</h3>
                            <p>jQuery UI is a curated set of user interface interactions, effects, widgets, and themes built on top of the jQuery JavaScript Library. Whether you're building highly interactive web applications or you just need to add a date picker to a form control, jQuery UI is the perfect choice.</p>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}

export default Profile;