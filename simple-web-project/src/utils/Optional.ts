/**
 * @version 1.1
 */
export class Optional{
  o:any;
  static of(o:any){
    let obj = new Optional();
    obj.o = o;
    return obj;
  }
  filter(func:any){
    if(!this.o || this.o == null || this.o == undefined){
      return this;
    }
    let b = func(this.o)
    if(b == true){

    }else{
      this.o = null
    }
    return this;
  }
  map(func:any){
    if(!this.o || this.o == null || this.o == undefined){
      return this;
    }
    this.o = func(this.o)
    return this;
  }
  get(){
    return this.o;
  }
  orElse(o:any){
    if(this.o && this.o != null && this.o != undefined){
      return this.o;
    }else{
      return o;
    }
  }
}
