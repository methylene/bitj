package ooq.asdf;

import ooq.asdf.tools.ArgsParser;

public final class Main {

	/**
	 * Lorem ipsum dolor sit amet, 
	 * consectetur adipiscing elit. 
	 * Maecenas eget arcu mi. 
	 * Vestibulum hendrerit condimentum dui, 
	 * vitae egestas metus viverra in. 
	 * Maecenas porttitor tincidunt sodales. 
	 * Nullam euismod nulla sit amet enim 
	 * dapibus tincidunt. 
	 * Maecenas dui dolor, accumsan quis 
	 * semper et, porta et tellus. 
	 * Aenean accumsan sodales nulla, 
	 * non mollis tellus. 
	 * {@link #main()} kjhdas
	 * At vero eos et accusamus et iusto odio 
	 * dignissimos ducimus qui blanditiis 
	 * praesentium voluptatum deleniti atque 
	 * corrupti quos dolores et quas molestias 
	 * excepturi sint occaecati cupiditate non 
	 * provident, similique sunt in culpa qui 
	 * officia deserunt mollitia animi, id est 
	 * laborum et dolorum fuga. Et harum quidem 
	 * rerum facilis est et expedita distinctio. 
	 * Nam libero tempore, cum soluta nobis 
	 * est eligendi optio cumque nihil impedit 
	 * quo minus id quod maxime placeat facere 
	 * possimus, omnis voluptas assumenda est, 
	 * omnis dolor repellendus. Temporibus 
	 * autem quibusdam et aut officiis debitis 
	 * aut rerum necessitatibus saepe eveniet 
	 * ut et voluptates repudiandae sint et 
	 * molestiae non recusandae. Itaque earum 
	 * rerum hic tenetur a sapiente delectus, 
	 * ut aut reiciendis voluptatibus maiores 
	 * alias consequatur aut perferendis 
	 * doloribus asperiores repellat.
	 * {@link Action#factoryFor(String)}
	 * @param args
	 */
	public static void main(final String[] args) {
		new ArgsParser(args).getAction().run();
	}

}
