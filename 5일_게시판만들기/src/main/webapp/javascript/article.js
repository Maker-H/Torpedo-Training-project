function checkUserInArticle(article) {
    console.log("글을 작성한 유저가 맞는지 확인");
    
    axios.get('')
        .then(response => {
            createDeleteBtn(article);
        })
        .catch(error => {
            console.log(error);
        });
}

function editArticle(articlePk) {
    window.location.href = 'editArticle.jsp?pk=' + articlePk;
}


// 게시물 삭제 버튼 추가
function createDeleteBtn(article) {
    const deleteButtonElement = document.getElementById('delete');
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '게시물 삭제';
    deleteButton.addEventListener('click', function() {
        deleteArticle(article.pk);
    });
    deleteButtonElement.appendChild(deleteButton);
}

function getArticleDetails(articlePk) {
    console.log('게시글 상세 정보 가져오기');
    
    axios.get(`articleDetail.do?pk=${articlePk}`)
        .then(response => {
            displayArticleDetails(response.data);
            getfileDetails(response.data.pk);
        })
        .catch(error => {
            console.log(error);
            if (!error){
                alert(error.response.data.statusDescription);
                window.location.href = error.response.headers.path;
            }
        });
}

function displayArticleDetails(article) {
    console.log(article);
    
    const articleTitleElement = document.getElementById('articleTitle');
    const articleUserElement = document.getElementById('articleUser');
    const articleContentElement = document.getElementById('articleContent');

    articleTitleElement.innerHTML = article.title;
    articleUserElement.innerHTML= '작성자: ' + article.user;
    articleContentElement.innerHTML= article.content;

    checkIfUserIsIdentical(article);
    
}

function getfileDetails(articlePk) {
    console.log('파일 정보 가져오기');
            
        axios.get(`articleFileDetail.do?pk=${articlePk}`)
        .then(response => {
            const files = response.data.files;
            for (const fileTitle in files) {
                makeFileURL(fileTitle, files[fileTitle]); // Base64 인코딩된 파일 데이터
            }
        })
        .catch(error => {
            console.log(error);
        });
}
function makeFileURL(fileTitle, base64Data){
    console.log('파일 다운로드 링크 만들기');
    const articleFileLinkElement = document.getElementById('articleFileLink');
    const blob = base64ToBlob(base64Data, 'application/octet-stream'); // Blob 객체 생성
    const fileURL = URL.createObjectURL(blob); // Blob URL 생성
    
    let fileLink = document.createElement('a');
    fileLink.href = fileURL;
    fileLink.download = fileTitle;
    
    fileLink.textContent = fileTitle; // 다운로드 링크에 표시할 텍스트
    
    articleFileLinkElement.appendChild(fileLink);
    articleFileLinkElement.appendChild(document.createElement('br')); // 각 파일마다 줄 바꿈 추가
}

function base64ToBlob(base64, contentType) {
    console.log('디코딩 시작');
    
    const byteCharacters = atob(base64);
    const byteArrays = [];
    
    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);
        
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    
    return new Blob(byteArrays, {type: contentType});
}

function deleteArticle(articlePk) {
    console.log("게시글 지우기", articlePk);
    axios.post('articleDelete.do', { pk: articlePk })
        .then(response => {
            // 게시물 삭제에 성공한 경우 추가적인 동작 수행
            console.log('게시물 삭제 성공');
            if(response.headers.path) {
                window.location.href = response.headers.path;
            }
            // 예를 들어, 삭제 후에 어떤 작업을 수행하거나 페이지를 새로고침할 수 있습니다.
        })
        .catch(error => {
            if (!error){
                alert(error.response.data.statusDescription);
                window.location.href = error.response.headers.path;
            }
        });
}

function checkIfUserIsIdentical(article) {
    console.log("글 작성자가 유저인지 확인");
    
        axios.post('userCheckSame.do', { pk: article.pk })
        .then(response => {
            loadSameUserBtn(response.data.isSameUser);
        })
        .catch(error => {
            console.log(error);
            console.log("작성자가 아닌 유저");
        });
}

function loadSameUserBtn(isSameUser) {
    if (isSameUser) {
        console.log("글을 작성한 유저 ");
        document.getElementById('deleteBtn').style.display = 'block';
        document.getElementById('updateBtn').style.display = 'block';
    }
}