export class Contact
{
    constructor(name: string, company: string, email: string, phone: string, interest: string) {
        this.Name = name;
        this.Company = company;
        this.Email = email;
        this.Phone = phone;
        this.Interest = interest;
    }

    public Name: string;

    public Company: string;

    public Email: string;

    public Phone: string;

    public Interest: string;
}