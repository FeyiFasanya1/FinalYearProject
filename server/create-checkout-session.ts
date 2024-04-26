// src/pages/api/create-checkout-session.ts

import { NextApiRequest, NextApiResponse } from 'next';
import { Stripe } from 'stripe';

const price_uk = "price_1OgYmfF6o7MyieNL4ORYZShi"
const price_us = "price_1Oer7QF6o7MyieNLqDh54DZA"

const stripe = new Stripe(process.env.STRIPE_API_KEY || '');

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method === 'POST') {
    try {
      const { totalPrice } = req.query
      console.log(totalPrice)
      const customer = await stripe.customers.create();
      const ephemeralKey = await stripe.ephemeralKeys.create(
        {customer: customer.id},
        {apiVersion: '2024-04-10'}
      );
      
      const paymentIntent = await stripe.paymentIntents.create({
        amount: Number(totalPrice),
        currency: 'eur',
        customer: customer.id,
        // In the latest version of the API, specifying the `automatic_payment_methods` parameter
        // is optional because Stripe enables its functionality by default.
        automatic_payment_methods: {
          enabled: true,
        },
      });

      res.json({
        paymentIntent: paymentIntent.client_secret,
        ephemeralKey: ephemeralKey.secret,
        customer: customer.id,
        publishableKey: process.env.STRIPE_PUBLISHIBLE_KEY || ""
      });
    } catch (error: any) {
      res.status(500).json({ error: error.message });
    }
  } else {
    res.setHeader('Allow', 'POST');
    res.status(405).end('Method Not Allowed');
  }
}
