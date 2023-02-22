import axios, { } from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import { faCheckCircle, faArrowLeft, faHeart } from "@fortawesome/free-solid-svg-icons";
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
    const [isLoading, setIsLoading] = useState(true);
    const [file, setFile] = useState();
    const [files, setFiles] = useState([]);
    const [fileName, setFileName] = useState('');
    const [postsPage, setPostsPage] = useState([]);
    const [postLoading, setPostLoading] = useState(true);
    const [pageNumber, setPageNumber] = useState(0);
    const [affair, setAffair] = useState('');
    const [content, setContent] = useState('');
    const [numResCar, setNumResCar] = useState(250);
    const [editUserName, setEditUserName] = useState('');
    const [editPhoneNumber, setEditPhoneNumber] = useState('');
    const [editEmail, setEditEmail] = useState('');
    const [editFullName, setEditFullName] = useState('');
    const [likedPosts, setLikedPosts] = useState([]);
    const [pageContent, setPageContent] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(url, { headers: headers }).then((res) => {
            setUser(res.data);
            setEditUserName(res.data.userName);
            setEditPhoneNumber(res.data.phoneNumber);
            setEditEmail(res.data.email);
            setEditFullName(res.data.fullName);
            setLikedPosts(res.data.likedPosts);

        }).catch((er) => {
            if (er.response.status === 404) {
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

    const createPost = (e) => {
        e.preventDefault();
        if (files.length > 4) {
            Swal.fire({
                icon: 'error',
                title: 'No se pueden subir más de 4 imágenes',
                showConfirmButton: false,
                timer: 1500
            })
        } else {
            axios.post("/post/", postBody, { headers: headers }).then((res) => {
                if (res.status === 201) {
                    if (files.length > 1) {
                        imgsPostSubmit(e, res.data.id);
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: '¡El post se ha publicado con éxito!',
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            window.location.reload();
                        })
                    }
                }
            }).catch((er) => {
                console.log(er);
                Swal.fire({
                    icon: 'error',
                    title: er.response.data.subErrors[0].message,
                    showConfirmButton: false,
                    timer: 1500
                })
            })
        }
    }

    const imgsPostSubmit = (e, postId) => {
        e.preventDefault();
        headers["Content-Type"] = "multipart/form-data"
        let formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i])
        }

        axios.post(`/file/upload/post/${postId}`, formData, { headers: headers }).then((res) => {
            if (res.status === 201) {
                Swal.fire({
                    icon: 'success',
                    title: '¡El post se ha publicado con éxito!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.reload();
                })
            }
        }).catch((er) => {
            console.log(er, files);
            Swal.fire({
                icon: 'error',
                title: er.response['data']['message'],
                showConfirmButton: false,
                timer: 1500
            })
        })
    }

    const postBody = {
        affair: affair,
        content: content
    }

    useEffect(() => {
        axios.get(`/post/?page=${pageNumber}`, { headers: headers }).then((res) => {
            setPostsPage(res.data);
            setPageContent(res.data.content);
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

    const likeAPost = (post) => {
        axios.post(`/post/like/${post.id}`, null, { headers: headers }).then(() => {
            window.location.reload();
        }).catch((er) => {
            console.log(er);
        })
    }

    const logOut = () => {
        localStorage.clear();
        navigate("/")
    }

    const editProfile = (e) => {
        e.preventDefault();
        axios.put(`/user/edit/profile`, editProfileBody, { headers: headers }).then((res) => {
            console.log(res);
            localStorage.setItem("loggedUser", editUserName);
        }).catch((er) => {
            console.log(er);
        })
    }

    const editProfileBody = {
        username: editUserName,
        email: editEmail,
        phoneNumber: editPhoneNumber,
        fullName: editFullName
    }

    const [boolPostForm, setBoolPostForm] = useState(false);
    var postForm = document.getElementById("postForm");
    var backgroundPostForm = document.getElementById("backgroundFormPost")
    var prog = document.getElementById("porcentajaProg");

    function mostrarPostForm() {
        if (boolPostForm) {
            postForm.style.top = "-1000px"
            backgroundPostForm.style.visibility = "hidden"
            setBoolPostForm(false);
        } else {
            postForm.style.top = "25%"
            backgroundPostForm.style.visibility = "visible"
            setBoolPostForm(true);
        }
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
                <button id="backgroundFormPost" onClick={() => mostrarPostForm()}>
                </button>
                <div id="postForm">
                    <form onSubmit={createPost}>
                        <input type="text" maxLength="50" onChange={(e) => setAffair(e.target.value)}></input>
                        <textarea onChange={(e) => { setContent(e.target.value); setNumResCar(250 - e.target.value.length); prog.style.width = (e.target.value.length * 70) / 250 + "px" }} maxLength="250"></textarea>
                        <div className="porcentaje">
                            <p>{numResCar}</p><div id="porcentajeBG">
                                <div id="porcentajaProg"></div>
                            </div>
                        </div>
                        <div className="files-upload">
                            <input type="file" name="file" id="file" multiple className="inputfile" onChange={(e) => {
                                setFiles(e.target.files);
                            }} />
                            <label htmlFor="file"><i className="fa fa-cloud-upload"></i> Seleccione un archivo</label>
                        </div>
                        <input type="submit" value="Postear" id="submitSaveImg"></input>
                    </form>
                </div>
                <nav id="navBar">
                    <button id="logOutButton" onClick={() => logOut()}>Log out</button>
                    <button id="miPerfilButton" onClick={() => navigate("/user/" + localStorage.getItem("loggedUser"))}>Mi perfil</button>
                    <button id="postearButton" onClick={() => mostrarPostForm()}>Postear</button>
                    <button id="goBack"><FontAwesomeIcon icon={faArrowLeft} size="2x" onClick={() => { window.history.back() }} /></button>
                </nav>
                <div id="fullBackground">
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
                                        {// eslint-disable-next-line
                                            postsPage !== undefined && postsPage.totalElements > 20 &&
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
                                            pageContent.map((p) => {
                                                var bool = false;
                                                if (likedPosts.filter((po) => po.id === p.id).length > 0) {
                                                    bool = true;
                                                }
                                                return <div className="post" key={p.id}>
                                                    <button className="userWhoPost" onClick={() => navigateTo(p.userWhoPost.userName)}>
                                                        <img src={`http://localhost:8080/file/${p.userWhoPost.imgPath}`} alt="" />
                                                        <p>{p.userWhoPost.userName}</p>
                                                        {p.userWhoPost.verified &&
                                                            <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" size="1x" className="verificadoUser" />
                                                        }
                                                    </button>
                                                    { bool &&
                                                        <FontAwesomeIcon icon={faHeart} className="likePostButton" onClick={() => likeAPost(p)} size={"2x"} color="red"/>
                                                    }
                                                    { !bool &&
                                                        <FontAwesomeIcon icon={faHeart} className="likePostButton" onClick={() => likeAPost(p)} size={"2x"} />
                                                    }
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
                                <h3>Modificar datos personales</h3>
                                <form onSubmit={editProfile}>
                                    <div id="editForm">
                                        <input type="text" placeholder="userName" value={editUserName} onChange={(e) => setEditUserName(e.target.value)}></input>
                                        <input type="text" placeholder="fullName" value={editFullName} onChange={(e) => setEditFullName(e.target.value)}></input>
                                        <input type="text" placeholder="email" value={editEmail} onChange={(e) => setEditEmail(e.target.value)}></input>
                                        <input type="text" placeholder="phoneNumber" value={editPhoneNumber} onChange={(e) => setEditPhoneNumber(e.target.value)}></input>
                                    </div>
                                    <input type="submit" value="Guardar" id="submitSaveImg"></input>
                                </form>
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
            </div>
        );
    }
}

export default Profile;