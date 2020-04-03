package com.manydesigns.portofino.model;

import org.apache.commons.configuration2.Configuration;

import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Entity implements ModelObject, Annotated {

    protected Domain domain;
    protected String name;
    protected final List<Property> properties = new ArrayList<>();
    protected final List<Property> id = new ArrayList<>();
    protected final List<Annotation> annotations = new ArrayList<>();
    protected final List<Relationship> relationships = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        setDomain((Domain) parent);
    }

    @Override
    public void reset() {

    }

    @Override
    public void init(Model model, Configuration configuration) {
        if(!properties.containsAll(id)) {
            throw new RuntimeException("Not all id properties belong to this entity"); //TODO
        }
    }

    @Override
    public void link(Model model, Configuration configuration) {

    }

    @Override
    public void visitChildren(ModelObjectVisitor visitor) {
        for(Property property : properties) {
            visitor.visit(property);
        }
        for (Annotation annotation : annotations) {
            visitor.visit(annotation);
        }
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        if(this.domain == domain) {
            return;
        }
        this.domain = domain;
        domain.addEntity(this);
    }

    public void addRelationship(Entity other) {
        Relationship r = new Relationship(this, other);
        domain.addRelationship(r);
    }

    public Collection<Relationship> getRelationships() {
        return relationships;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Property> getId() {
        return id;
    }
}
