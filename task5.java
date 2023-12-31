import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Contact {
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nPhone Number: " + phoneNumber + "\nEmail Address: " + emailAddress;
    }
}


class AddressBook {
    private DefaultListModel<Contact> contactListModel = new DefaultListModel<>();

    public void addContact(Contact contact) {
        contactListModel.addElement(contact);
    }

    public void removeContact(Contact contact) {
        contactListModel.removeElement(contact);
    }

    public Contact searchContact(String name) {
        for (int i = 0; i < contactListModel.size(); i++) {
            Contact contact = contactListModel.getElementAt(i);
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }

    public DefaultListModel<Contact> getContactListModel() {
        return contactListModel;
    }
}

public class task5 {
    private AddressBook addressBook = new AddressBook();
    private JList<Contact> contactList;
    private String dataFilePath = "addressbook.txt"; 

    public task5() {
        loadContacts(); 

        JFrame frame = new JFrame("Address Book System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        contactList = new JList<>(addressBook.getContactListModel());
        panel.add(new JScrollPane(contactList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        JButton removeButton = new JButton("Remove Contact");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeContact();
            }
        });

        JButton searchButton = new JButton("Search Contact");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });

        JButton editButton = new JButton("Edit Contact");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(editButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog("Enter name:");
        if (name != null) {
            String phoneNumber = JOptionPane.showInputDialog("Enter phone number:");
            String emailAddress = JOptionPane.showInputDialog("Enter email address:");
            Contact contact = new Contact(name, phoneNumber, emailAddress);
            addressBook.addContact(contact);
            saveContacts(); 
        }
    }

    private void removeContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            Contact selectedContact = contactList.getSelectedValue();
            addressBook.removeContact(selectedContact);
            saveContacts(); 
        }
    }

    private void searchContact() {
        String name = JOptionPane.showInputDialog("Enter name to search:");
        if (name != null) {
            Contact foundContact = addressBook.searchContact(name);
            if (foundContact != null) {
                JOptionPane.showMessageDialog(null, foundContact, "Contact Found", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Contact not found.", "Contact Not Found", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void editContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            Contact selectedContact = contactList.getSelectedValue();
            String newName = JOptionPane.showInputDialog("Enter new name:", selectedContact.getName());
            if (newName != null) {
                String newPhoneNumber = JOptionPane.showInputDialog("Enter new phone number:", selectedContact.getPhoneNumber());
                String newEmailAddress = JOptionPane.showInputDialog("Enter new email address:", selectedContact.getEmailAddress());

                
                selectedContact.setName(newName);
                selectedContact.setPhoneNumber(newPhoneNumber);
                selectedContact.setEmailAddress(newEmailAddress);

                contactList.repaint();
                saveContacts(); 
            }
        }
    }

    private void saveContacts() {
        try (PrintWriter writer = new PrintWriter(dataFilePath)) {
            DefaultListModel<Contact> model = addressBook.getContactListModel();
            for (int i = 0; i < model.size(); i++) {
                Contact contact = model.getElementAt(i);
                writer.println(contact.getName());
                writer.println(contact.getPhoneNumber());
                writer.println(contact.getEmailAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            List<Contact> contacts = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String name = line;
                String phoneNumber = reader.readLine();
                String emailAddress = reader.readLine();
                contacts.add(new Contact(name, phoneNumber, emailAddress));
            }

            for (Contact contact : contacts) {
                addressBook.addContact(contact);
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new task5();
            }
        });
    }
}
