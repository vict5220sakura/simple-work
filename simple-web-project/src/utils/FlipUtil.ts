export class Flip{
    
    element:any;
    first:any;
    last:any;
    duration:number;
    transitionEndHandler: any;
    isPlaying: boolean;

    // 记录初始位置
    constructor(element:any, duration:number = 0.5){
        this.element = element
        this.first = element.getBoundingClientRect();
        this.isPlaying = false
        this.duration = duration

        this.transitionEndHandler = () => {
            this.isPlaying = false;
            // this.recordFirst();
        };
    };

    invert = ()=>{

        if (!this.isPlaying) {
            this.element.style.removeProperty('transition');

            this.last = this.element.getBoundingClientRect();
            const deltaX = this.first.left - this.last.left;
            const deltaY = this.first.top - this.last.top;
            const deltaW = this.first.width / this.last.width;
            const deltaH = this.first.height / this.last.height;
            
            
            this.element.style.transform = `
                translate(${deltaX}px, ${deltaY}px) 
            `;

            this.isPlaying = true
        }


        requestAnimationFrame(()=>{
            this.element.style.transition = 'transform ' + this.duration + 's';
            requestAnimationFrame(()=>{
                this.element.style.removeProperty('transform');

                this.element.removeEventListener('transitionend', this.transitionEndHandler);
                this.element.addEventListener('transitionend', this.transitionEndHandler);
            })
        })
    }
}

export class FlipList{
    flipList: any[];
    constructor(elementList:any[], duration:number = 0.5){
        this.flipList = []
        for(let element of elementList){
            let flip = new Flip(element, duration)
            this.flipList.push(flip)
        }
    }
    invert = ()=>{
        for(let flip of this.flipList){
            flip.invert()
        }
    }
}