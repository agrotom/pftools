package com.at.pftools20.logic.props.main;

/**
 * Declaring variable with that interface makes properties are immutable.
 *
 */
public sealed interface ImmutableProperty<T> permits BaseProperty<T>
{
	String toString();
	
	String toJSON();
	
	T getValue();
}
