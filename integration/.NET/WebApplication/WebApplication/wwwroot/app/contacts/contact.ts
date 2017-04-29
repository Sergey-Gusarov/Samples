export class Contact
{

    constructor(id: number, externalId: string, name: string, company: string, email: string, phone: string, interest: string) {
        this.Id = id;
        this.ExternalId = externalId;
        this.Name = name;
        this.Company = company;
        this.Email = email;
        this.Phone = phone;
        this.Interest = interest;
    }

    public Id: number;
    public ExternalId: string;
    public Name: string;
    public Company: string;
    public Email: string;
    public Phone: string;
    public Interest: string;
}