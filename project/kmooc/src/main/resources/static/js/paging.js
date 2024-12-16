let pageBtn = document.querySelectorAll(".page-btn");

for(let i of pageBtn){
    i.addEventListener("click",(e)=>{
        e.preventDefault();
        e.stopPropagation();
        const num = e.target.getAttribute("data-num");
        const formObj = document.querySelector("form");
        formObj.innerHTML += `<input type='hidden' name='pageNo' value='${num}'>`;
        formObj.submit();
    });
}