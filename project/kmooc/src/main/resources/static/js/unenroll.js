async function unenroll(element){
    const courseId = element.getAttribute("data-courseId")
    try {
        const response = await fetch(`/courseEnrollment/delete/${courseId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            console.log("response not ok");
            const error = await response.json();
            console.log(error.message);
            alert(error.message);
            return;
        }
        console.log("response ok");
        const result = await response.json();
        console.log(result);
        alert(result.message);
        location.reload();
    } catch (error) {
        console.error('수강취소 에러:', error);
        alert(error.message || '수강취소에 실패했습니다.');
    }
}