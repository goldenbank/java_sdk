function  getBasePath(){
    let path = window.location.pathname.split("/");
    let basePath = path[0]+"/"+path[1]+"/";
    return basePath;
}