class Category {
  //private name: string;
  private readonly taxPercentage: number;

  constructor(name: string, taxPercentage: number) {
    //this.name = name;
    this.taxPercentage = taxPercentage;
  }

  public getTaxPercentage(): number {
    return this.taxPercentage;
  }

}

export default Category;

