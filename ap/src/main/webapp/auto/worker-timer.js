var worker_timeout = new Worker('timer-worker.js');

var workerTimer_timeout = {
  id: 0,
  callbacks: {},

  setInterval: function(cb, interval, context) {
    this.id++;
    var id = this.id;
    this.callbacks[id] = { fn: cb, context: context };
    worker_timeout.postMessage({ command: 'interval:start', interval: interval, id: id });
    return id;
  },

  onMessage: function(e) {
    switch (e.data.message) {
      case 'interval:tick':
        var callback = this.callbacks[e.data.id];
        if (callback && callback.fn) callback.fn.apply(callback.context);
        break;
      case 'interval:cleared':
        delete this.callbacks[e.data.id];
        break;
    }
  },

  clearInterval: function(id) {
    worker_timeout.postMessage({ command: 'interval:clear', id: id });
  }
};

worker_timeout.onmessage = workerTimer_timeout.onMessage.bind(workerTimer_timeout);

 