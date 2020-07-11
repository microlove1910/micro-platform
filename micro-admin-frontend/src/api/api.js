import request from '@/utils/request';

class Api {

  get(url, params) {
    let queryString = [];
    Object.keys(params).forEach(key => params[key] != undefined && queryString.push(`${key}=${params[key]}`));
    if (queryString.length > 0) {
      queryString = queryString.join('&');
      url += `?${queryString}`;
    }
    return request({
      url: url,
      method: 'get',
    });
  }

  post(url, data) {
    return request({
      url: url,
      method: 'post',
      data,
    });
  }

  put(url, data) {
    return request({
      url: url,
      method: 'put',
      data,
    });
  }

  delete(url, params) {
    return request({
      url: url,
      method: 'delete',
      params: params,
    });
  }

  upload(url, data) {
    return request({
      url: url,
      method: 'post',
      data: data,
    })
  }

  uploadImage(url, params, event) {
    return new Promise((resolve, reject) => {
      const file = event.file;
      if (file.type.indexOf('image') === -1) {
        return reject('请上传图片类型文件');
      }
      const isLt4M = file.size / 1024 / 1024 < 4;
      if (!isLt4M) {
        return reject('图片大小不能超过 4MB');
      }

      const formData = new FormData();
      formData.append('file', file);
      Object.keys(params).forEach(v => formData.append(v, params[v]));

      this.upload(url, formData).then(res => {
        return resolve(res);
      });
    });
  }
}

export default Api;
